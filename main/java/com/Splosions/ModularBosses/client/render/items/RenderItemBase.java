package com.Splosions.ModularBosses.client.render.items;


import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import com.Splosions.ModularBosses.ModularBosses;
import com.Splosions.ModularBosses.client.models.item.ModelBait;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class RenderItemBase implements IItemRenderer
{

	
	public static final float SCALE = 0.0625f;

	protected ModelBase model;
	protected ResourceLocation texture;
	
	protected float baseScale = 1.0f;

	protected float scale_thirdp = 0.35f;
	protected float scale_ground = 0.5f;
	protected float scale_ego = 0.5f;
	protected float scale_gui = 0.4f;
	protected float scale_itemframe = 0.5f;

	protected float[] translateBase = { 0f, 0f, 0f };

	protected float[][] translateType = { { 0f, 0f, 0f }, // TRANSLATE FIRST
			// PERSON
			{ 0f, 0f, 0f }, // TRANSLATE THIRD PERSON
			{ 0f, 0f, 0f }, // TRANSLATE GUI
			{ 0f, 0f, 0f }, // TRANSLATE GROUND
			{ 0f, 0f, -0.05f } // TRANLATE FIXED (frame)
	};

	protected int parts = 1;

	public RenderItemBase(ModelBase model, ResourceLocation texture) {
		super();
		this.model = model;
		this.texture = texture;
	}

	public RenderItemBase setGUIScale(float guiscale) {
		this.scale_gui = guiscale;
		return this;
	}
	
	public RenderItemBase setFirstPersonScale(float scale) {
		this.scale_ego = scale;
		return this;
	}
	
	public RenderItemBase setGroundAndFrameScale(float scale) {
		this.scale_ground = scale;
		this.scale_itemframe=scale;
		return this;
	}

	/**
	 * Set the basic translation applied to ALL types
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public RenderItemBase setBaseTranslation(float x, float y, float z) {
		this.translateBase[0] = x;
		this.translateBase[1] = y;
		this.translateBase[2] = z;
		return this;
	}

	/**
	 * TRANSLATE FIRST PERSON x,y,z
	 * TRANSLATE THIRD PERSON x,y,z
	 * TRANSLATE GUI x,y,z
	 * TRANSLATEGROUND x,y,z
	 * TRANLATE FIXED (frame) x,y,z
	 * 
	 * Left hand gets automatically mirrored
	 * 
	 * @param translations
	 *            - must be a float[5][3]
	 * @return
	 */
	public RenderItemBase setTransformTranslations(float[][] translations) {
		this.translateType = translations;
		return this;
	}
	
	public RenderItemBase setBaseScale(float baseScale) {
		this.baseScale = baseScale;
		return this;
	}

	protected float getScaleFactorFromTransform(TransformType transform) {
		switch (transform) {
		case FIRST_PERSON_LEFT_HAND:
		case FIRST_PERSON_RIGHT_HAND:
			return baseScale * scale_ego;

		case THIRD_PERSON_LEFT_HAND:
		case THIRD_PERSON_RIGHT_HAND:
			return baseScale * scale_thirdp;

		case GUI:
			return baseScale * scale_gui;

		case GROUND:
			return baseScale * scale_ground;

		case FIXED:
			return baseScale * scale_itemframe;

		default:
			return baseScale;

		}
	}

	protected void applyTranslation(TransformType transform) {
		int index = -1;
		boolean flip = false;

		switch (transform) {
		case FIRST_PERSON_LEFT_HAND:
			flip = true; // fallthrough
		case FIRST_PERSON_RIGHT_HAND:
			index = 0;
			break;

		case THIRD_PERSON_LEFT_HAND:
			flip = true; // fallthrough
		case THIRD_PERSON_RIGHT_HAND:
			index = 1;
			break;
		case GUI:
			index = 2;
			break;
		case GROUND:
			index = 3;
			break;
		case FIXED:
			index = 4;
			break;
		default:
			break;
		}
		if (index >= 0) {
			float mirror = flip?-1.0f:1.0f;
			//GlStateManager.translate((translateType[index][0]+Keybinds.X)*mirror, translateType[index][1]+Keybinds.Y, translateType[index][2]+Keybinds.Z);
			GlStateManager.translate((translateType[index][0])*mirror, translateType[index][1], translateType[index][2]);
		}
	}
	
	/**
	 * center the model
	 */
	protected void applyBaseTranslation(){
		GlStateManager.translate(this.translateBase[0], this.translateBase[1], this.translateBase[2]);
		//GlStateManager.translate(this.translateBase[0]+Keybinds.X, this.translateBase[1]+Keybinds.Y, this.translateBase[2]+Keybinds.Z);
	}
	
	
	@Override
	public void renderItem(TransformType transform, ItemStack stack, EntityLivingBase elb, boolean leftHanded) {
		
			GlStateManager.pushMatrix();

			Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

			this.applyTranslation(transform);

			if (TransformType.FIRST_PERSON_LEFT_HAND == transform || TransformType.FIRST_PERSON_RIGHT_HAND == transform) {

			} else if (TransformType.THIRD_PERSON_LEFT_HAND == transform || TransformType.THIRD_PERSON_RIGHT_HAND == transform) {

			} else if (TransformType.GUI == transform) {
				GlStateManager.rotate(40.0f, 0, 1f, 0);
				GlStateManager.rotate(20.0f, 1f, 0, 0);

			} else if (TransformType.GROUND == transform) {

			} else if (TransformType.FIXED == transform) {
				GlStateManager.rotate(-90.0f, 0, 1.0f, 0);
			}

			this.setBaseScale(transform);
			this.setBaseRotation(transform);
			this.applyBaseTranslation();
			
			for (int i = 0; i < parts; i++) {
				model.render(elb, 0, 0, 0, 0, 0, SCALE);
			}

			GlStateManager.popMatrix();

	}

	protected void setBaseScale(TransformType transform) {
		float scale = getScaleFactorFromTransform(transform);
		GlStateManager.scale(scale, scale, scale);
	}

	protected void setBaseRotation(TransformType transform) {
		GlStateManager.rotate(-180.0f, 1.0f, 0, 0);
		GlStateManager.rotate(180.0f, 0f, 1.0f, 0);
	}

}